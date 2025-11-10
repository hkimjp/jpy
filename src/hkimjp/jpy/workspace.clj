(ns hkimjp.jpy.workspace
  (:require [hkimjp.jpy.view :refer [page]]))

(defn index [_request]
  (page [:div "workspace"]))
