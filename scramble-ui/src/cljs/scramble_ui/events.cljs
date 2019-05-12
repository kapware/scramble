(ns scramble-ui.events
  (:require
   [re-frame.core :as re-frame]
   [scramble-ui.db :as db]
   [day8.re-frame.http-fx]
   [ajax.core :as ajax]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))


(re-frame/reg-event-fx
 ::dictionary-changed
 (fn [{db :db} [_ new-val]]
   {:db (-> db
            (assoc :dictionary new-val)
            (dissoc :result :error))}))


(re-frame/reg-event-fx
 ::word-changed
 (fn [{db :db} [_ new-val]]
   {:db (-> db
            (assoc :word new-val)
            (dissoc :result :error))}))


(re-frame/reg-event-fx
 ::verify-scramble
 (fn [{:keys [db]} _]
   (let [{:keys [dictionary word]} db]
     {:db   (assoc db :spinning? true)
      :http-xhrio {:method          :get
                   :uri             (str "http://localhost:9999/api/v1/scramble"
                                         "?dict=" dictionary
                                         "&word=" word)
                   :timeout         8000
                   :response-format (ajax/json-response-format {:keywords? true})
                   :on-success      [::verify-scramble-success]
                   :on-failure      [::verify-scramble-failed]}})))


(re-frame/reg-event-fx
 ::verify-scramble-success
 (fn [{:keys [db]} [_ resp]]
   {:db (-> db
            (assoc :spinning? false
                   :result resp)
            (dissoc :error))}))


(re-frame/reg-event-fx
 ::verify-scramble-failed
 (fn [{:keys [db]} [_ resp]]
   {:db (-> db
            (assoc :spinning? false
                   :error (str "Error: " resp))
            (dissoc :result))}))
