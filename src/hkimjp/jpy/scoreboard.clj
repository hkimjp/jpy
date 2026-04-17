(ns hkimjp.jpy.scoreboard
  (:require
   [charred.api :as charred]
   ; [clojure.edn :as edn]
   ; [clojure.java.io :as io]
   [environ.core :refer [env]]
   [org.httpkit.client :as hk-client]
   [hkimjp.jpy.util :as u]
   [hkimjp.jpy.view :refer [page]]))

;; Accept:application/edn
(defn users []
  (sort (mapv :login (-> @(hk-client/get (env :users))
                         :body
                         (charred/read-json :key-fn keyword)))))

;; different smiles week by week
(def smile (constantly "😀"))

(defn- submits [author]
  (map (fn [[e dt]] [:a {:hx-get (str "/answers/answer/" e)
                         :hx-target (str "#" author)}
                     (smile dt)])
       (u/submits author)))

(defn index [{{:keys [identity]} :session}]
  (page
   [:div.m-4
    [:div.text-2xl.font-medium "scoreboard"]
    (for [user (users)]
      [:div
       [:div.flex.mx-4
        (if (= identity user)
          [:div {:class "w-24"} [:span.text-white.bg-red-500 user]]
          [:div {:class "w-24"} user])
        [:div (submits user)]]
       [:div.mx-4 {:id user}]])]))
