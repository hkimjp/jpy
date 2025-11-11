(ns hkimjp.jpy.workspace
  (:require
   [hiccup2.core :as h]
   [java-time.api :as jt]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   [taoensso.telemere :as tel]
   [hkimjp.datascript :as ds]
   [hkimjp.jpy.util :refer [btn user]]
   [hkimjp.jpy.view :refer [page error-page]]))

(def list-answers-q
  '[:find ?e ?num
    :in $ ?author
    :where
    [?e :login ?author]
    [?e :p/num ?num]])

(defn list-answers [author]
  (into
   [:div.flex.gap-x-4]
   (for [[e num] (ds/qq list-answers-q author)]
     [:span (str num)])))

(comment
  (list-answers "hkimura")
  :rcf)

(def ^:private current-problem-id
  '[:find ?e ?current
    :where
    [?e :current ?current]])

(def ^:private current-problem
  '[:find [?e ?num ?problem]
    :in $ ?num
    :where
    [?e :num ?num]
    [?e :problem ?problem]])

(comment
  (ds/qq current-problem-id)
  (apply max-key first (ds/qq current-problem-id))
  (ds/qq current-problem 4)
  :rcf)

(defn index [request]
  (let [author (user request)
        [_ id] (apply max-key first (ds/qq current-problem-id))
        [e num problem] (ds/qq current-problem id)]
    (tel/log! {:level :info :id "index" :data {:e e :num num :p problem}})
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
      [:div.font-bold "answers"]
      (list-answers author)])))

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

