(ns hkimjp.jpy.admin
  (:require
   [clojure.string :as str]
   [environ.core :refer [env]]
   [hiccup2.core :as h]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   [taoensso.telemere :as tel]
   [hkimjp.jpy.event :refer [number-of-clients]]
   [hkimjp.jpy.problems :as problems]
   [hkimjp.jpy.util :as util :refer [btn]]
   [hkimjp.jpy.view :refer [page]]))

(defn number-of-clients-section []
  [:div.flex.gap-x-4
   [:div.font-bold "number of clients"]
   [:div.border-1.px-2 (number-of-clients)]
   [:div {:class btn} [:a {:href "/admin/reset"} "reset clients"]]])

(defn new-problem-section []
  [:div
   [:div.font-bold "new problem"]
   [:form.m-4 {:method "post" :action "/problems/create"}
    (h/raw (anti-forgery-field))
    [:textarea {:class "w-full h-20 p-2 border-1" :name "problem"}]
    [:button {:class btn :type "submit"} "create"]]])

(defn problems-section []
  (let [current (:id (util/current-problem))]
    [:div
     [:div.font-bold "problems"]
     (into
      [:div#list-all.mx-4
       [:form {:method "post" :action "/problems/current"}
        (h/raw (anti-forgery-field))
        (for [{:keys [e _ problem]} (problems/problems)]
          [:div.flex.gap-x-4
           [:button {:class btn :name "current" :value e}
            (if (= current e) "✔️" "⬜️")]
           [:div e] [:div problem]])]])]))

(defn env-vars-section []
  [:div.my-4
   [:div.font-bold "Env Vars"]
   (for [e (-> [:develop :port :auth :admin :datascript :redis
                :python-path :pytest-path :ruff-path :tz]
               sort)]
     [:div (-> e symbol str str/upper-case) ": " (env e)])])

(defn admin [_request]
  (tel/log! :info "admin")
  (page
   [:div.m-4 [:div.text-2xl.font-medium "Admin"]
    (number-of-clients-section)
    (new-problem-section)
    (problems-section)
    (env-vars-section)]))
