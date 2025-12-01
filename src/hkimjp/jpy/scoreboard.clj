(ns hkimjp.jpy.scoreboard
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [hkimjp.jpy.util :refer [score]]
   [hkimjp.jpy.view :refer [page]]))

(def users (edn/read-string (slurp (io/resource "users.txt"))))

(def smile (constantly "😀"))

(defn- submits [author]
  (map (fn [x] (-> x first smile)) (score author)))

(defn index [{{:keys [identity]} :session}]
  (page
   [:div.m-4
    [:div.text-2xl.font-medium "scoreboard"]
    (for [user users]
      [:div.flex.gap-x-4
       [:div {:class "w-24"} user]
       [:div (submits user)]])]))
