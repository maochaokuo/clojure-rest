(ns clojure-rest.handler
  (:use compojure.core)
  (:require ;[compojure.core :refer :all]
    [compojure.handler :as handler]
    [compojure.route :as route]
    ;[ring.middleware.defaults :refer [wrap-defaults site-defaults]]
    ))

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
