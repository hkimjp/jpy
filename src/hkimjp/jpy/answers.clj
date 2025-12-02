(ns hkimjp.jpy.answers
  (:require
   [clojure.string :as str]
   [java-time.api :as jt]
   [taoensso.telemere :as tel]
   [hkimjp.datascript :as ds]
   [hkimjp.jpy.util :as util]
   [hkimjp.jpy.validate :refer [validate]]
   [hkimjp.jpy.view :refer [error-page redirect hx]]))

(defn answer-hx
  "called from answers-section, returns hx response"
  [{{:keys [e]} :path-params}]
  (tel/log! {:level :info :id "answer" :msg e})
  (let [{:keys [answer p/id datetime]} (ds/pl (parse-long e))
        {:keys [problem]} (ds/pl id)]
    (hx [:div
         [:p problem]
         [:pre.p-2 answer]
         [:p (jt/format "YYYY-MM-dd hh:mm:ss" datetime)]])))

(defn upload!
  [{{:keys [login answer]} :params}]
  (let [{:keys [id]} (util/current-problem)]
    (tel/log! {:level :info
               :data {:login login :p/id id :answer answer}})
    (try
      (when (str/starts-with? answer "def")
        (tel/log! :debug "validation started")
        (validate login answer "" nil))
      (ds/put! {:login login
                :p/id id
                :answer answer
                :datetime (jt/local-date-time)})
      (redirect "/workspace")
      (catch Exception e
        (tel/log! {:level :error
                   :id "upload!"
                   :data {:login login :answer answer}})
        (error-page (.getMessage e))))))

