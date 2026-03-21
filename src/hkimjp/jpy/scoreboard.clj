(ns hkimjp.jpy.scoreboard
  (:require
   [charred.api :as charred]
   ; [clojure.edn :as edn]
   ; [clojure.java.io :as io]
   [environ.core :refer [env]]
   [org.httpkit.client :as client]
   [hkimjp.jpy.util :refer [score]]
   [hkimjp.jpy.view :refer [page]]))

(defonce users (mapv :login (-> @(client/get (env :users))
                                :body
                                (charred/read-json :key-fn keyword))))

(def smile (constantly "😀"))

(defn- submits [author]
  (map (fn [x] (-> x first smile)) (score author)))

(defn index [{{:keys [identity]} :session}]
  (page
   [:div.m-4
    [:div.text-2xl.font-medium "scoreboard"]
    (for [user users]
      [:div.flex.gap-x-4
       (if (= identity user)
         [:div {:class "w-24 text-white bg-red-500"} user]
         [:div {:class "w-24"} user])
       [:div (submits user)]])]))
