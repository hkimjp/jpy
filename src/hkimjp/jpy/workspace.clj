(ns hkimjp.jpy.workspace
  (:require
   [hiccup2.core :as h]
   [java-time.api :as jt]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   [taoensso.telemere :as tel]
   [hkimjp.datascript :as ds]
   [hkimjp.jpy.util :refer [btn user]]
   [hkimjp.jpy.view :refer [page error-page]]))

(def ^:private current-problem-id
  '[:find ?e ?current
    :where
    [?e :current ?current]])

(comment
  (ds/qq current-problem-id)
  (apply max-key first (ds/qq current-problem-id))
  (ds/qq '[:find [?e ?num ?problem]
           :in $ ?num
           :where
           [?e :num ?num]
           [?e :problem ?problem]]
         4)
  (ds/pl 9) (ds/qq '[:find ?e
                     :where
                     [?e :num 4]])
  :rcf)

(defn index [request]
  (let [author (user request)
        [_ id] (apply max-key first (ds/qq current-problem-id))
        [e num problem] (ds/qq '[:find [?e ?num ?problem]
                                 :in $ ?num
                                 :where
                                 [?e :num ?num]
                                 [?e :problem ?problem]]
                               id)]
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

