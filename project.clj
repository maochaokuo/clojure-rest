(defproject clojure-rest "0.1.0-SNAPSHOT"
  :description "REST service for documents"
  :url "https://tutswiki.com"
  :min-lein-version "2.0.0"
  :source-paths ["src"]
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [metosin/reitit "0.5.2"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-json "0.1.2"]
                 [ring-cors "0.1.13"]
                 [http-kit "2.4.0-alpha6"]
                 [c3p0/c3p0 "0.9.1.2"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [com.h2database/h2 "1.3.168"]
                 [cheshire "4.0.3"]
                 ]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler clojure-rest.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}}
  )
