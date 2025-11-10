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
   [hkimjp.jpy.view :refer [page hx]]))

(defn new-problem []
  [:div
   [:div.font-bold "new problem"]
   [:form.m-4 {:method "post"}
    (h/raw (anti-forgery-field))
    [:textarea {:class "w-full h-20 p-2 border-1" :name "problem"}]
    [:button {:class     btn
              :hx-post   "/admin/create"
              :hx-target "#list-all"
              :hx-swap   "afterbegin"}
     "create"]]])

(def ^:private find-max-q
  '[:find [(max ?num)]
    :where
    [?e :num ?num]])

; (-> (ds/qq find-max-q) first inc)

(def ^:private current-q
  '[:find ?num
    :where
    [?e :current ?num]
    [?e :avail "yes"]])

; (ds/qq current-q)

(defn upsert-current [c]
  (let [[cur] (ds/qq current-q)]
    (ds/put! {:db/id cur :current c})))

(defn create! [{{:keys [problem]} :params}]
  (let [num (-> (ds/qq find-max-q) first inc)]
    (tel/log! {:level :info :id "create!" :data {:num num :problem problem}})
    (try
      (ds/put! {:num num
                :avail "yes"
                :problem problem
                :datetime (jt/local-date-time)})
      (upsert-current num)
      (hx [:div.flex.gap-x-4 [:div (str num)] [:div problem]])
      (catch Exception e
        (tel/log! {:level :warn
                   :id "create!"
                   :msg (:getMessage e)})))))

(def problems-q
  '[:find ?num ?problem
    :keys num problem
    :where
    [?e :num ?num]
    [?e :problem ?problem]])

; (ds/qq problems-q)

(defn problems []
  [:div
   [:div.font-bold "problems"]
   (into
    [:div#list-all.mx-4
     (for [p (->> (ds/qq problems-q) (sort-by :num) reverse)]
       [:div.flex.gap-x-4 [:div (:num p)] [:div (:problem p)]])])])

(defn env-vars []
  [:div.my-4
   [:div.font-bold "Env Vars"]
   (for [e [:develop :port :auth :admin :datascript]]
     [:div.mx-4 (-> e symbol str str/upper-case) ": " (env e)])])

(defn admin [_request]
  (page
   [:div.m-4 [:div.text-2xl.font-medium "Admin"]
    (new-problem)
    (problems)
    (env-vars)]))


