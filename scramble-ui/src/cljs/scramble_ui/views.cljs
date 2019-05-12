(ns scramble-ui.views
  (:require
   [re-frame.core      :as re-frame]
   [scramble-ui.subs   :as subs]
   [scramble-ui.events :as events]
   [antizer.reagent    :as ant]))

(defn target-value [evt]
  (-> evt .-target .-value))

(defn main-panel []
  (let [dictionary (re-frame/subscribe [::subs/dictionary])
        word       (re-frame/subscribe [::subs/word])
        result     (re-frame/subscribe [::subs/result])
        error      (re-frame/subscribe [::subs/error])
        spinning   (re-frame/subscribe [::subs/spinning])]
    [ant/layout
     [ant/layout-header
      [:div.logo]
      [ant/menu
       {:theme "dark"
        :mode  "horizontal"}
       [ant/menu-item "Scramble?"]]]

     [ant/layout-content
      [ant/layout
       [ant/layout-content
        [ant/form
         [ant/spin {:spinning @spinning}
          [ant/card {:title "Subanagram verifier" :bordered true}
           [ant/form-item {:label "Dictionary (enter letters [a-z])"}
            [ant/input {:value     @dictionary
                        :on-change (fn [evt] (re-frame/dispatch [::events/dictionary-changed (target-value evt)]))}]]
           [ant/form-item {:label "Word to check against dictionary (enter letters [a-z])"}
            [ant/input {:value     @word
                        :on-change (fn [evt] (re-frame/dispatch [::events/word-changed (target-value evt)]))}]]
           [ant/button {:on-click (fn [] (re-frame/dispatch [::events/verify-scramble]))} "Scramble? check"]

           (if (some? @result)
             [ant/alert {:message (if @result "You can form a word from given dictionary"
                                      "Nope. You can't rearrange those letters to for this word, Jack") :type "info"}])

           (if (some? @error)
             [ant/alert {:message @error :type "error"}])

           ]]]]]]
     [ant/layout-footer "©2019 kapware.com"]]))
