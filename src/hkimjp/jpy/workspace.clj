(ns hkimjp.jpy.workspace
  (:require
   [hiccup2.core :as h]
   [java-time.api :as jt]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   [taoensso.telemere :as tel]
   [hkimjp.datascript :as ds]
   [hkimjp.jpy.util :refer [btn user]]
   [hkimjp.jpy.view :refer [page error-page hx redirect]]))

(defn answer [{{:keys [e]} :path-params}]
  (tel/log! {:level :info :id "answer" :msg e})
  (hx [:pre (:answer (ds/pl (parse-long e)))]))

(def list-answers-q
  '[:find ?e ?num
    :in $ ?author
    :where
    [?e :login ?author]
    [?e :p/num ?num]])

(defn list-answers-by [author]
  [:div
   [:div.font-bold "answers"]
   (into
    [:div.flex.gap-x-4]
    (for [[e num] (->> (ds/qq list-answers-q author) (sort-by first))]
      [:a.underline {:hx-get (format "/workspace/answer/%d" e)
                     :hx-target "#answer"} num]))
   [:div#answer.my-4]])

;(list-answers-by "hkimura")

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

(defn index [request]
  (let [author (user request)
        [_ id] (apply max-key first (ds/qq current-problem-id))
        [e num problem] (ds/qq current-problem id)]
    (tel/log! {:level :info :id "index" :data {:e e :num num :p problem}})
    (page
     [:div.m-4
      [:div.text-2xl.font-medium "workspace"]
      [:div.flex.gap-x-4 [:div num] [:div problem]]
      [:form {:method "post"}
       (h/raw (anti-forgery-field))
       [:input {:type "hidden" :name "login" :value author}]
       [:input {:type "hidden" :name "num" :value num}]
       [:textarea {:class "w-full h-64 border-1 p-2"
                   :name "answer"
                   :placeholder "your answer, please."}]
       [:button {:class btn} "submit"]]
      [:br]
      (list-answers-by author)])))

(defn upload! [{{:keys [login num answer]} :params :as request}]
  (tel/log! {:level :info
             :id "upload!"
             :data (dissoc (:params request) :__anti-forgery-token)})
  (try
    (ds/put! {:login login :p/num num :answer answer :datetime (jt/local-date-time)})
    (redirect "/workspace/")
    (catch Exception e
      (error-page (.getMessage e)))))

