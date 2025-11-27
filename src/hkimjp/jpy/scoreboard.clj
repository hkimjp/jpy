(ns hkimjp.jpy.scoreboard
  (:require
   [clojure.edn :refer [read-string]]
   [clojure.java.io :as io]
   [hkimjp.jpy.util :refer [score]]
   [hkimjp.jpy.view :refer [page]]))

(def users (read-string (slurp (io/resource "users.txt"))))

(def smile (constantly "😀"))

(defn- submits [author]
  (map (fn [x] (-> x first smile)) (score author)))

(defn index [{{:keys [identity]} :session}]
  (page
   [:div.m-4
    [:div.text-2xl.font-medium "scoreboard " identity]
    (for [user users]
      [:div.flex.gap-x-4
       [:div {:class "w-24"} user]
       [:div (submits user)]])]))
