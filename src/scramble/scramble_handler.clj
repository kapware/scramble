(ns scramble.scramble-handler
  (:require [compojure.api.sweet     :as api :refer [context defroutes resource]]
            [clojure.tools.logging   :as log]
            [ring.util.http-response :as http-response :refer [created ok not-found]]
            [scramble.scramble       :as scramble]
            [spec-tools.core         :as st]
            [clojure.spec.alpha      :as s]
            [spec-tools.data-spec    :as ds]))

(s/def ::result boolean?)
(s/def ::dict ::scramble/scramblie)
(s/def ::word ::scramble/scramblie)


(defroutes routes
  (context "/scramble" []
           :tags     ["scramble"]
           :coercion :spec

           (context "/" []
                    (resource
                     {:get
                      {:responses  {200 {:schema ::result}}
                       :parameters {:query-params (s/keys :opt-un [::dict
                                                                   ::word])}
                       :handler    (fn [{{:keys [dict word]} :query-params}]
                                     (ok (scramble/scramble? dict word)))}}))))
