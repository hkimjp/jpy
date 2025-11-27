(ns hkimjp.jpy.workspace
  (:require
   [hiccup2.core :as h]
   [java-time.api :as jt]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   [taoensso.telemere :as tel]
   [hkimjp.jpy.util :as util :refer [btn user]]
   [hkimjp.jpy.view :refer [page]]))

(defn- shorten- [datetime]
  (let [[mm dd] (jt/as datetime :month-of-year :day-of-month)]
    (str mm "-" dd)))

; (shorten- (jt/local-date-time))
;(->> (util/list-answers "hkimura") (sort-by first))

(defn answers-section [author]
  [:div.my-4
   [:div.font-bold "answers"]
   (into
    [:div.flex.gap-x-4]
    (for [[e num datetime] (->> (util/list-answers author) (sort-by first))]
      [:a.underline {:hx-get (str "/answers/answer/" e)
                     :hx-target "#answer"} (shorten- datetime)]))
   [:div#answer.my-4]])

(defn index [request]
  (let [author (user request)
        {:keys [id problem]} (util/current-problem)]
    (tel/log! {:level :info :id "index" :data {:id id :problem problem}})
    (page
     [:div.m-4
      [:div.text-2xl.font-medium "workspace"]
      [:div.my-4
       [:div {:hx-ext "sse" :sse-connect "/event" :sse-swap "message"}
        problem]
       [:form {:method "post" :action "/answers/upload"}
        (h/raw (anti-forgery-field))
        [:input {:type "hidden" :name "login" :value author}]
        [:textarea {:class "w-full h-64 border-1 p-2"
                    :name "answer"
                    :placeholder "your answer, please."}]
        [:button {:class btn} "submit"]]]
      (answers-section author)])))

