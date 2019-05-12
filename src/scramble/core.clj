(ns scramble.core
  (:gen-class)
  (:require [scramble.scramble-handler :as scramble-handler]
            [clojure.tools.logging     :as log]
            [mount.core                :as mount :refer [defstate]]
            [org.httpkit.server        :as httpkit]
            [compojure.route           :as route]
            [compojure.api.sweet       :as api :refer [api context defroutes resource]]
            [ring.middleware.cors      :refer [wrap-cors]]))


(def handler
  (api
   {:swagger
    {:ui   "/"
     :spec "/swagger.json"
     :data {:info {:title "Scramble api"
                   :description "Scramble api"}
            :tags [{:name "api", :description "scramble apis"}]}}}
   (context "/api/v1" []
            :tags ["api"]
            :coercion :spec
            scramble-handler/routes)
   (route/not-found "Nothing here")))


(defn start-webservice []
  (let [port 9999] ;; TODO: configure from env
    (log/info (str "Starting web, port :" port))
    (httpkit/run-server (-> handler
                            (wrap-cors
                             ;; Allow figwheel for now, this needs to be configurable
                             :access-control-allow-origin [#"http://0.0.0.0:3449"]
                             :access-control-allow-methods [:get]))
                        {:port port})))


(defstate webservice 
  :start (start-webservice)
  ;; (start-webservice) is expected to return a function to close webservice
  :stop (webservice))


(defn -main [& args]
  (mount/start
   #'scramble.core/webservice))


