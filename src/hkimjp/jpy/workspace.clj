(ns hkimjp.jpy.workspace
  (:require
   [hiccup2.core :as h]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   [taoensso.telemere :as tel]
   [hkimjp.datascript :as ds]
   [hkimjp.jpy.util :refer [btn user]]
   [hkimjp.jpy.view :refer [page]]))

(def current-q
  '[:find [?num ?problem]
    :where
    [?e :current ?num]
    [?num :problem ?problem]])

; (ds/qq current-q)

(defn index [request]
  (let [[num problem] (ds/qq current-q)]
    (tel/log! {:level :info :id "index"})
    (page
     [:div.m-4
      [:div.text-2xl.font-medium "workspace"]
      [:div.gap-x-4 [:span (str num)] problem]
      [:form {:method "post"}
       (h/raw (anti-forgery-field))
       [:input {:type "hidden" :name "login" :value (user request)}]
       [:input {:type "hidden" :name "num" :value num}]
       [:textarea.w-full.h-80.border-1
        {:name "answer" :placeholder "your answer, please."}]
       [:button {:class btn} "submit"]]])))

(defn upload! [{{:keys [login num answer]} :params :as request}]
  (tel/log! {:level :info
             :id "upload!"
             :data (dissoc (:params request) :__anti-forgery-token)})
  (page [:div "upload!"]))
