(ns hkimjp.jpy.workspace
  (:require
   [hiccup2.core :as h]
   [java-time.api :as jt]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   [taoensso.telemere :as tel]
   [hkimjp.datascript :as ds]
   [hkimjp.jpy.util :refer [btn user]]
   [hkimjp.jpy.view :refer [page error-page]]))

(def ^:private current-problem
  '[:find [?num ?problem]
    :where
    [?e :current ?num]
    [?num :problem ?problem]])

(defn index [request]
  (let [author (user request)
        [num problem] (ds/qq current-problem)]
    (tel/log! {:level :info :id "index" :data {:num num :author author}})
    (page
     [:div.m-4
      [:div.text-2xl.font-medium "workspace"]
      [:div.flex.gap-x-4 [:div (str num)] [:div problem]]
      [:form {:method "post"}
       (h/raw (anti-forgery-field))
       [:input {:type "hidden" :name "login" :value author}]
       [:input {:type "hidden" :name "num" :value num}]
       [:textarea.w-full.h-80.border-1
        {:name "answer" :placeholder "your answer, please."}]
       [:button {:class btn} "submit"]]
      [:br]
      [:div.font-bold "answers"]])))

(defn upload! [{{:keys [login num answer]} :params :as request}]
  (tel/log! {:level :info
             :id "upload!"
             :data (dissoc (:params request) :__anti-forgery-token)})
  (try
    (ds/put! {:login login :p/num num :answer answer :datetime (jt/local-date-time)})
    (page
     [:div "upload success. " login ", " num])
    (catch Exception e
      (error-page (.getMessage e)))))

