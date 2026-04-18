(ns hkimjp.jpy.scoreboard
  (:require
   [charred.api :as charred]
   ; [clojure.edn :as edn]
   ; [clojure.java.io :as io]
   [java-time.api :as jt]
   [environ.core :refer [env]]
   [org.httpkit.client :as hk-client]
   [hkimjp.jpy.util :as u]
   [hkimjp.jpy.view :refer [page]]
   [hkimjp.carmine :as c]))

;; Accept:application/edn
(defn users []
  (sort (mapv :login (-> @(hk-client/get (env :users))
                         :body
                         (charred/read-json :key-fn keyword)))))

(def smiles
  {"04-14" "😁"
   "04-21" "🤠"
   "04-28" "😊"
   "05-12" "🤡"
   "05-19" "🤗"
   "05-26" "👻"
   "06-02" "😳"
   "06-09" "😺"
   "06-16" "🙄"
   "06-23" "🎃"
   "06-30" "🥹"
   "07-07" "🤖"
   "07-14" "😋"
   "07-21" "🤨"
   "07-28" "👾"
   "08-04" "🤔"})

;; different smiles week by week
(defn smile [dt]
  (get smiles (jt/format "MM-dd" dt)))

(defn- submits [author]
  (map (fn [[e dt]] [:a {:hx-get (str "/answers/answer/" e)
                         :hx-target (str "#" author)}
                     (smile dt)])
       (u/submits author)))

(defn index-raw [{{:keys [identity]} :session}]
  (page
   [:div.m-4
    [:div.text-2xl.font-medium "scoreboard"]
    (for [user (users)]
      [:div
       [:div.flex.mx-4
        ; if use cache, this is not.
        ; (if (= identity user)
        ;   [:div {:class "w-24"} [:span.text-white.bg-red-500 user]]
        ;   [:div {:class "w-24"} user])
        [:div {:class "w-24"} user]
        [:div (submits user)]]
       [:div.mx-4 {:id user}]])]))

(def ttl (parse-long (or (env :ttl) "60")))

(defmacro cache-page [uri handler args]
  `(if-let [cached# (c/get ~uri)]
     cached#
     (let [result# (~handler ~args)]
       (c/setex ~uri ttl result#)
       result#)))

(defn index [request]
  (cache-page (:uri request) index-raw request))
