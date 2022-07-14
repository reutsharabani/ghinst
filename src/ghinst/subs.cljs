(ns ghinst.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::things
  :things)

(re-frame/reg-sub
  ::new-thing
  :new-thing)

(re-frame/reg-sub
  ::thing-1
  :thing-1)

(re-frame/reg-sub
  ::thing-2
  :thing-2)

(re-frame/reg-sub
  ::sorting
  :sorting)
