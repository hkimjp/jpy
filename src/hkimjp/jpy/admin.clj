(ns hkimjp.jpy.admin
  (:require
   [clojure.string :as str]
   [environ.core :refer [env]]
   [hiccup2.core :as h]
   #_[java-time.api :as jt]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   [taoensso.telemere :as tel]
   #_[hkimjp.datascript :as ds]
   [hkimjp.jpy.problem :as problem]
   [hkimjp.jpy.util :refer [btn]]
   [hkimjp.jpy.view :refer [page]]))

(defn env-vars-section []
  [:div
   [:div.font-bold "Env Vars"]
   (for [e [:develop :port :auth :admin :datascript :redis]]
     [:div (-> e symbol str str/upper-case) ": " (env e)])])

(defn new-problem-section []
  [:div
   [:div.font-bold "new problem"]
   [:form.m-4 {:method "post"}
    (h/raw (anti-forgery-field))
    [:textarea {:class "w-full h-20 p-2 border-1" :name "problem"}]
    [:button {:class     btn
              :hx-post   "/problem/create"
              :hx-target "#list-all"
              :hx-swap   "afterbegin"}
     "create"]]])

#_(def ^:private find-max-q
    '[:find [(max ?num)]
      :where
      [?e :num ?num]])

; (-> (ds/qq find-max-q) first inc)

#_(def ^:private current-q
    '[:find ?num
      :where
      [?e :current ?num]
      [?e :avail "yes"]])

#_(defn create! [{{:keys [problem]} :params}]
    (let [num (-> (ds/qq find-max-q) first inc)]
      (tel/log! {:level :info :id "create!" :data {:num num :problem problem}})
      (try
        (ds/put! {:num num
                  :avail true
                  :problem problem
                  :datetime (jt/local-date-time)})
        (ds/put! {:current num})
        (hx [:div.flex.gap-x-4 [:div (str num)] [:div problem]])
        (catch Exception e
          (tel/log! {:level :warn :id "create!"
                     :msg (:getMessage e)})))))

(defn problems-section []
  [:div
   [:div.font-bold "problems"]
   (into
    [:div#list-all.mx-4
     (for [p (problem/problems)]
       [:div.flex.gap-x-4 [:div (:num p)] [:div (:problem p)]])])])

(defn admin [_request]
  (tel/log! :info "admin")
  (page
   [:div.m-4 [:div.text-2xl.font-medium "Admin"]
    (new-problem-section)
    (problems-section)
    (env-vars-section)]))


