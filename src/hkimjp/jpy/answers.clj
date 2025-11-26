(ns hkimjp.jpy.answers
  (:require
   [java-time.api :as jt]
   [taoensso.telemere :as tel]
   [hkimjp.datascript :as ds]
   [hkimjp.jpy.util :as util]
   [hkimjp.jpy.view :refer [error-page redirect hx]]))

(defn answer
  "called from answers-section, returns hx response"
  [{{:keys [e]} :path-params}]
  (tel/log! {:level :info :id "answer" :msg e})
  (let [{:keys [answer p/id]} (ds/pl (parse-long e))
        {:keys [problem]} (ds/pl id)]
    (hx [:div
         [:p problem]
         [:pre answer]])))

(defn upload! [{{:keys [login answer]} :params :as request}]
  (let [{:keys [id]} (util/current-problem)]
    (tel/log! {:level :info
               :data {:login login :p/id id :answer answer}})
    (try
      (ds/put! {:login login
                :p/id id
                :answer answer
                :datetime (jt/local-date-time)})
      (redirect "/workspace")
      (catch Exception e
        (error-page (.getMessage e))))))

