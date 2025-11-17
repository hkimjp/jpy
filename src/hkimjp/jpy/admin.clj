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

(defn problems-section []
  (let [current (problem/current-num)]
    [:div
     [:div.font-bold "problems"]
     (into
      [:div#list-all.mx-4
       [:form {:method "post" :action "/problem/current"}
        (h/raw (anti-forgery-field))
        (for [{:keys [e valid num problem]} (problem/problems)]
          [:div.flex.gap-x-4
           [:button {:class btn :name "current" :value e}
            (if (= current num) "✔️" "⬜️")]
           [:div e] [:div (str valid)] [:div num] [:div problem]])]])]))

(comment
  (problem/current-num)
  :rcf)

(defn admin [_request]
  (tel/log! :info "admin")
  (page
   [:div.m-4 [:div.text-2xl.font-medium "Admin"]
    (new-problem-section)
    (problems-section)
    (env-vars-section)]))


