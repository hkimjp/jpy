(ns hkimjp.jpy.workspace
  (:require
   [hiccup2.core :as h]
   ; [java-time.api :as jt]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   [taoensso.telemere :as tel]
   [hkimjp.datascript :as ds]
   [hkimjp.jpy.answers :as answers]
   [hkimjp.jpy.util :as util :refer [btn user]]
   [hkimjp.jpy.view :refer [page error-page hx redirect]]))

#_(defn answer
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
      [:a.underline {:hx-get (format "/answers/answer/%d" e)
                     :hx-target "#answer"} num]))
   [:div#answer.my-4]])

; (ds/qq util/current-problem)

(defn index [request]
  (let [author (user request)
        [id problem] (ds/qq util/current-problem)]
    (tel/log! {:level :info :id "index" :data {:id id :problem problem}})
    (page
     [:div.m-4
      [:div.text-2xl.font-medium "workspace"]
      ;;
      [:div.my-4
       #_[:div (format "%d %s" id problem)] ; <- event
       [:div {:hx-ext "sse" :sse-connect "/event" :sse-swap "message"}
        "waiting an event"]
       [:form {:method "post" :action "/answers/upload"}
        (h/raw (anti-forgery-field))
        [:input {:type "hidden" :name "login" :value author}]
        #_[:input {:type "hidden" :name "id" :value id}] ; <-
        [:textarea {:class "w-full h-64 border-1 p-2"
                    :name "answer"
                    :placeholder "your answer, please."}]
        [:button {:class btn} "submit"]]]
      ;;
      (answers-section author)])))

; (index {})
