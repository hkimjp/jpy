(ns hkimjp.jpy.scoreboard
  (:require [hkimjp.jpy.view :refer [page]]))

(defn index [_request]
  (page [:div "scoreboard"]))
