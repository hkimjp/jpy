(ns hkimjp.jpy.workspace
  (:require
   [hiccup2.core :as h]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   [taoensso.telemere :as tel]
   [hkimjp.jpy.util :refer [list-answers mm-dd current-problem btn]]
   [hkimjp.jpy.view :refer [page]]))

(defn answers-section [author]
  [:div.my-4
   [:div.font-bold "answers"]
   (into
    [:div.flex.gap-x-4]
    (for [[e _ datetime] (->> (list-answers author) (sort-by first))]
      [:a.underline {:hx-get (str "/answers/answer/" e)
                     :hx-target "#answer"} (mm-dd datetime)]))
   [:div#answer.my-4]])

(defn index [{{:keys [identity]} :session}]
  (let [{:keys [id problem]} (current-problem)]
    (tel/log! {:level :info :id "index" :data {:id id :problem problem}})
    (page
     [:div.m-4
      [:div.text-2xl.font-medium "workspace"]
      [:div.my-4
       [:div {:hx-ext "sse" :sse-connect "/event" :sse-swap "message"}
        problem]
       [:form {:method "post" :action "/answers/upload"}
        (h/raw (anti-forgery-field))
        [:input {:type "hidden" :name "login" :value identity}]
        [:textarea {:class "w-full h-64 border-1 p-2"
                    :name "answer"
                    :placeholder "your answer, please."}]
        [:button {:class btn} "submit"]]]
      (answers-section identity)])))

