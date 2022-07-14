(ns ghinst.views
  (:require
   [re-frame.core :as re-frame]
   [ghinst.subs :as subs]
   ))

(defn list-of-things []
  (let [things (re-frame/subscribe [::subs/things])]
    (into [:ul] (map vector (repeat :li) @things))))

(defn sort-button []
  [:input {:type "button"
           :value "sort"
           :on-click (fn [e]
                       (.preventDefault e)
                       (re-frame/dispatch [:sort]))}])

(defn new-thing-input []
  (let [new-thing (re-frame/subscribe [::subs/new-thing])]
    [:input {:type "text"
             :value @new-thing
             :on-change (fn [e]
                          (.preventDefault e)
                          (re-frame/dispatch [:edit (-> e .-target .-value)]))}]))

(defn add-thing-button []
  [:input {:type "button"
           :value "+"
           :on-click (fn [e]
                       (.preventDefault e)
                       (re-frame/dispatch [:add]))}])

(defn comparator []
  (let [sorting (re-frame/subscribe [::subs/sorting])
        thing-1 (re-frame/subscribe [::subs/thing-1])
        thing-2 (re-frame/subscribe [::subs/thing-2])]
    (when @sorting
      [:div
       "which is better?"
       [:input {:type "button"
                :value @thing-1
                :on-click (fn [e]
                            (.preventDefault e)
                            (re-frame/dispatch [:choose @thing-1 @thing-2]))}]
       [:input {:type "button"
                :value @thing-2
                :on-click (fn [e]
                            (.preventDefault e)
                            (re-frame/dispatch [:choose @thing-2 @thing-1]))}]])))

(defn sorter []
  [:div
   [new-thing-input]
   [add-thing-button]
   [list-of-things]
   [:br]
   [sort-button]
   [comparator]])

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [sorter]]))
