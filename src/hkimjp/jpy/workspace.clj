(ns hkimjp.jpy.workspace
  (:require
   [hiccup2.core :as h]
   [java-time.api :as jt]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   [taoensso.telemere :as tel]
   [hkimjp.datascript :as ds]
   [hkimjp.jpy.answers :as answers]
   [hkimjp.jpy.util :refer [btn user]]
   [hkimjp.jpy.view :refer [page error-page hx redirect]]))

(defn answer
  "called from answers-section, returns hx response"
  [{{:keys [e]} :path-params}]
  (tel/log! {:level :info :id "answer" :msg e})
  (let [{:keys [answer p/id]} (ds/pl (parse-long e))
        {:keys [problem]} (ds/pl id)]
    (hx [:div
         [:p problem]
         [:pre answer]])))

(defn answers-section [author]
  [:div.my-4
   [:div.font-bold "answers"]
   (into
    [:div.flex.gap-x-4]
    (for [[e num] (->> (answers/list-answers author) (sort-by first))]
      [:a.underline {:hx-get (format "/workspace/answer/%d" e)
                     :hx-target "#answer"} num]))
   [:div#answer.my-4]])

(def ^:private current-problem
  '[:find [?id ?problem]
    :where
    [?e :current ?id]
    [?id :problem ?problem]])

; (ds/qq current-problem)
; => [266 "11/18 の授業は終わりました。"]

(defn index [request]
  (let [author (user request)
        [id problem] (ds/qq current-problem)]
    (tel/log! {:level :info :id "index" :data {:id id :problem problem}})
    (page
     [:div.m-4
      [:div.text-2xl.font-medium "workspace"]
      ;;
      [:div.my-4
       [:div (format "%d %s" id problem)] ; <-
       [:form {:method "post"}
        (h/raw (anti-forgery-field))
        [:input {:type "hidden" :name "login" :value author}]
        [:input {:type "hidden" :name "id" :value id}] ; <-
        [:textarea {:class "w-full h-64 border-1 p-2"
                    :name "answer"
                    :placeholder "your answer, please."}]
        [:button {:class btn} "submit"]]]
      ;;
      (answers-section author)])))

; (index {})

;; shoule be in answer.clj?
(defn upload! [{{:keys [login id answer]} :params :as request}]
  (tel/log! {:level :info
             :id "upload!"
             :data (dissoc (:params request) :__anti-forgery-token)})
  (try
    (ds/put! {:login login
              :p/id (parse-long id) ; <-
              :answer answer
              :datetime (jt/local-date-time)})
    (redirect "/workspace")
    (catch Exception e
      (error-page (.getMessage e)))))
