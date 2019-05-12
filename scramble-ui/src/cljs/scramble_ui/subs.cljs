(ns scramble-ui.subs
  (:require
   [re-frame.core :as re-frame]))


(re-frame/reg-sub
 ::dictionary
 (fn [db]
   (:dictionary db)))


(re-frame/reg-sub
 ::word
 (fn [db]
   (:word db)))


(re-frame/reg-sub
 ::result
 (fn [db]
   (:result db)))


(re-frame/reg-sub
 ::error
 (fn [db]
   (:error db)))


(re-frame/reg-sub
 ::spinning
 (fn [db]
   (:spinning db)))
