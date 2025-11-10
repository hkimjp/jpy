(ns hkimjp.jpy.admin
  (:require
   [clojure.string :as str]
   [environ.core :refer [env]]
   [hiccup2.core :as h]
   [java-time.api :as jt]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   [hkimjp.datascript :as ds]
   [hkimjp.jpy.util :refer [btn]]
   [hkimjp.jpy.view :refer [page]]))

(def env-vars
  [:div
   [:div.font-bold "Env Vars"]
   (for [e [:develop :port :auth :admin :datascript]]
     [:div (-> e symbol str str/upper-case) ": " (env e)])])

(def new
  [:div
   [:form {:method "post"}
    (h/raw (anti-forgery-field))
    [:textarea {:class "w-full h-20 p-2 border-1" :name "problem"}]
    [:button {:class btn
              :hx-post   "/admin/create!"
              :hx-target "#list-all"
              :hx-swap   "afterbegin"} "new"]]])

(def find-max-q
  '[:find [(max ?num)]
    :where
    [?e :num ?num]])

; (-> (ds/qq find-max-q) first)

(defn create! [{{:keys [problem]} :params}]
  (ds/put! {:num (-> (ds/qq find-max-q) inc)
            :avail "yes"
            :probem problem
            :datetime (jt/local-date-time)}))

(def problems-q
  '[:find ?num ?problem
    :keys num problem
    :where
    [?e :num ?num]
    [?e :problem ?problem]])

; (ds/qq problems-q)

(defn list-all []
  [:div
   [:div.font-bold "problems"]
   (into
    [:div#list-all]
    (for [p (->> (ds/qq problems-q) (sort-by :num) reverse)]
      [:div [:span (:num p)] [:span (:problem p)]]))])

; (list-all)

(defn admin [_request]
  (page
   [:div.m-4 [:div.text-2xl.font-medium "Admin"]
    new
    (list-all)
    env-vars]))


