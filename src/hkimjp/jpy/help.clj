(ns hkimjp.jpy.help
  (:require [hkimjp.jpy.view :refer [page]]))

(defn help [_request]
  (page
   [:div.m-4
    [:div.text-2xl.font-medium "授業中 Python"]
    [:br]
    [:div.font-bold "workspace"]
    [:div.font-bold "scoreboard"]
    [:div.font-bold "login"]
    [:div.font-bold "admin"]]))

