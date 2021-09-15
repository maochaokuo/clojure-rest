(ns clojure-rest.handler
  (:import com.mchange.v2.c3p0.ComboPooledDataSource)
  (:use compojure.core)
  (:use cheshire.core)
  (:use ring.util.response)
  (:require [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [clojure.java.jdbc :as sql]
            [compojure.route :as route]
            [org.httpkit.server :refer [run-server]]
            [cheshire.core :as json]))

(def db-config
  {:classname "org.h2.Driver"
   :subprotocol "h2"
   :subname "mem:documents"
   :user ""
   :password ""})

(defn pool
  [config]
  (let [cpds (doto (ComboPooledDataSource.)
       (.setDriverClass (:classname config))
       (.setJdbcUrl (str "jdbc:" (:subprotocol config) ":" (:subname config)))
       (.setUser (:user config))
       (.setPassword (:password config))
       (.setMaxPoolSize 1)
       (.setMinPoolSize 1)
       (.setInitialPoolSize 1))]
    {:datasource cpds}))

(def pooled-db (delay (pool db-config)))

(defn db-connection [] @pooled-db)

(sql/with-connection (db-connection)
   ;  (sql/drop-table :documents) ; no need to do that for in-memory databases
   (sql/create-table :documents [:id "varchar(256)" "primary key"]
       [:title "varchar(1024)"]
       [:text :varchar]))

(defn uuid [] (str (java.util.UUID/randomUUID)))

(defn get-all-documents []
  (response
    (sql/with-connection (db-connection)
       (sql/with-query-results results
           ["select * from documents"]
           (into [] results)))))

(defn get-document [id]
  (sql/with-connection (db-connection)
     (sql/with-query-results results
       ["select * from documents where id = ?" id]
       (cond
         (empty? results) {:status 404}
         :else (response (first results))))))

(defn request-body->map
  [request]
  (-> request
      ;:body
      slurp
      (json/parse-string true)))
;;(json/parse-string (slurp (:body request)) true)

(defn create-new-document [doc]
  (let [id (uuid)]
    (sql/with-connection (db-connection)
       (let [document (assoc doc "id" id)]
         (println document) ; {id 1, title 1a2 title, text 1a2 text} 帥啊！直接印可以
         (sql/insert-record :documents document)))
    (get-document id)))

(defn update-document [id doc]
  (sql/with-connection (db-connection)
     (let [document (assoc doc "id" id)]
       (sql/update-values :documents ["id=?" id] document)))
  (get-document id))

(defn delete-document [id]
  (sql/with-connection (db-connection)
     (sql/delete-rows :documents ["id=?" id]))
  {:status 204})

(defroutes app-routes
 (context "/documents" [] (defroutes documents-routes
   (GET  "/" [] (get-all-documents))
   (POST "/" {body :body}
     (println body)
     (create-new-document body))
   (context "/:id" [id] (defroutes document-routes
     (GET    "/" [] (get-document id))
     (PUT    "/" {body :body} (update-document id body))
     (DELETE "/" [] (delete-document id))))))
 (route/not-found "Not Found"))

(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)))

(defonce server (atom nil))

(defn -main []
  (println "Server started port 4004")
  (reset! server (run-server app {:port 4004})))

(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)
    ;(println "Server stopped")
    ))

(defn restart-server []
  (stop-server)
  (-main))

(comment
  (restart-server)
  (app)
  )
