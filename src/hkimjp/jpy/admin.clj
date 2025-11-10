(ns hkimjp.jpy.admin
  (:require
   [clojure.string :as str]
   [environ.core :refer [env]]
   [hiccup2.core :as h]
   [java-time.api :as jt]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   [taoensso.telemere :as tel]
   [hkimjp.datascript :as ds]
   [hkimjp.jpy.util :refer [btn]]
   [hkimjp.jpy.view :refer [page]]))

(def env-vars
  [:div
   [:div.font-bold "Env Vars"]
   (for [e [:develop :port :auth :admin :datascript]]
     [:div (-> e symbol str str/upper-case) ": " (env e)])])

(defn new-problem []
  [:form {:method "post"}
   (h/raw (anti-forgery-field))
   [:textarea {:class "w-full h-20 p-2 border-1" :name "problem"}]
   [:button {:class btn
             :hx-post   "/admin/create"
             :hx-target "#list-all"
             :hx-swap   "afterbegin"}
    "create"]])

(def find-max-q
  '[:find [(max ?num)]
    :where
    [?e :num ?num]])

; (-> (ds/qq find-max-q) first inc)

(defn create! [{{:keys [problem]} :params}]
  (tel/log! {:level :info :id "create!" :msg problem})
  (try
    (ds/put! {:num (-> (ds/qq find-max-q) first inc)
              :avail "yes"
              :probem problem
              :datetime (jt/local-date-time)})
    [:div [:span num] [:span problem]]
    (catch Exception e
      (tel/log! {:level :warn
                 :id "create!"
                 :msg (:getMessage e)}))))

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
    (new-problem)
    (list-all)
    env-vars]))


