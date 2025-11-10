(ns hkimjp.jpy.scoreboard
  (:require [hkimjp.jpy.view :refer [page]]))

(defn index [_request]
  (page
   [:div.m-4
    [:div.text-2xl.font-medium "scoreboard"]
    [:div "under constuction"]]))
