(ns ghinst.events
  (:require
   [re-frame.core :as re-frame]
   [ghinst.db :as db]
   ))

(re-frame/reg-event-db
  ::initialize-db
  (fn [_ _]
    db/default-db))

(re-frame/reg-event-db
  :add
  (fn [db _]
    (when-let [new-thing (:new-thing db)]
      (-> db
          (update :things conj new-thing)
          (assoc :new-thing "")))))

(re-frame/reg-event-db
  :edit
  (fn [db [_ new-thing]]
    (assoc db :new-thing new-thing)))

(defn relations->comparator
  [relations]
  (fn [thing-1 thing-2]
    (or (get-in relations [thing-1 thing-2])
        (throw (ex-info (str "no relation between " thing-1 " and " thing-2)
                        {:thing-1 thing-1
                         :thing-2 thing-2}
                        :missing-relation)))))

(re-frame/reg-event-db
  :sort
  (fn [db _]
    (try
      (let [comparator (-> db
                           :relations
                           relations->comparator)
            sorted-things (sort comparator (:things db))]
        (-> db
            (assoc :things (reverse sorted-things))
            (assoc :sorting false)
            (assoc :relations {})))
      (catch js/Error e
        (let [data (ex-data e)]
          (-> db
              ;; add current things that need to be sorted to db
              (merge data)
              (assoc :sorting true)))))))

(re-frame/reg-event-fx
  :choose
  (fn [cofx [_ thing-1 thing-2]]
    {:db (-> cofx
             :db
             (update :relations assoc-in [thing-1 thing-2] 1)
             (update :relations assoc-in [thing-2 thing-1] -1))
     :dispatch [:sort]}))
