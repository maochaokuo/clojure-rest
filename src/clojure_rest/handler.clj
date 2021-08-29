(ns clojure-rest.handler
  (:import com.mchange.v2.c3p0.ComboPooledDataSource)
  (:use compojure.core)
  (:use cheshire.core)
  (:use ring.util.response)
  (:require [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [clojure.java.jdbc :as sql]
            [compojure.route :as route]))

(defroutes app-routes
  (context "/documents" [] (defroutes documents-routes
   (GET  "/" [] (get-all-documents))
   (POST "/" {body :body} (create-new-document body))
   (context "/:id" [id] (defroutes document-routes
     (GET    "/" [] (get-document id))
     (PUT    "/" {body :body} (update-document id body))
     (DELETE "/" [] (delete-document id))))))
  (route/not-found "Not Found"))

(def app
  ;(wrap-defaults app-routes site-defaults)
  ;(handler/site app-routes)
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response))
  )
