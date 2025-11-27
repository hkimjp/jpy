(ns hkimjp.jpy.scoreboard
  (:require
   [clojure.edn :refer [read-string]]
   [clojure.java.io :as io]
   [hkimjp.jpy.util :refer [score mm-dd]]
   [hkimjp.jpy.view :refer [page]]))

(def users (read-string (slurp (io/resource "users.txt"))))

(defn- submits [author]
  (map (fn [x] (-> x first mm-dd)) (score author)))

(defn index [{{:keys [identity]} :session}]
  (page
   [:div.m-4
    [:div.text-2xl.font-medium "scoreboard " identity]
    (for [user users]
      [:div.flex.gap-x-4
       [:div user]
       [:div (submits user)]])]))
