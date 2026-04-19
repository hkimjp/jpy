(ns hkimjp.jpy.answers
  (:require
   [clojure.string :as str]
   [environ.core :refer [env]]
   [java-time.api :as jt]
   [taoensso.telemere :as tel]
   [hkimjp.datascript :as ds]
   [hkimjp.jpy.util :as util]
   [hkimjp.jpy.validate :refer [validate]]
   [hkimjp.jpy.view :refer [error-page redirect hx]]
   [clojure.core :as c]))

(defn answer-hx
  "called from answers-section, returns hx response"
  [{{:keys [e]} :path-params}]
  (tel/log! {:level :info :id "answer" :msg e})
  (let [{:keys [answer p/id datetime]} (ds/pl (parse-long e))
        {:keys [problem]} (ds/pl id)]
    (hx [:div
         [:p problem]
         [:pre.p-2 answer]
         [:p (jt/format "YYYY-MM-dd HH:mm:ss" datetime)]])))

(defn ->sec
  [hhmm]
  (let [[h m] (str/split hhmm #":")]
    (+ (* (parse-long h) 60) (parse-long m))))

(defn- in-time?
  "judge current time is in answerable time"
  []
  (when-not (<= (->sec (env :start-time))
                (->sec (jt/format "HH:mm" (jt/local-time)))
                (->sec (env :end-time)))
    (throw (Exception. "not in time"))))

(defn upload!
  [{{:keys [login answer]} :params}]
  (let [{:keys [id]} (util/current-problem)]
    (tel/log! {:level :info
               :data {:login login :p/id id :answer answer}})
    (try
      (in-time?)
      (when (str/starts-with? answer "def")
        (tel/log! :debug "validation started")
        (validate login answer "" nil))
      (ds/put! {:login login
                :p/id id
                :answer answer
                :datetime (jt/local-date-time)})
      (redirect "/workspace")
      (catch Exception e
        (tel/log! {:level :error :id "upload!"
                   :data {:login login :answer answer
                          :e e}})
        (error-page (.getMessage e))))))
