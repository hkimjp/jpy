(ns hkimjp.jpy.answers
  (:require
   [java-time.api :as jt]
   [taoensso.telemere :as tel]
   [hkimjp.datascript :as ds]
   [hkimjp.jpy.view :refer [error-page redirect hx]]))

(def list-answers-q
  '[:find ?e ?num
    :in $ ?author
    :where
    [?e :login ?author]
    [?e :p/id ?num]])

(defn list-answers [author]
  (ds/qq list-answers-q author))

; (list-answers "hkimura")

(defn answer
  "called from answers-section, returns hx response"
  [{{:keys [e]} :path-params}]
  (tel/log! {:level :info :id "answer" :msg e})
  (let [{:keys [answer p/id]} (ds/pl (parse-long e))
        {:keys [problem]} (ds/pl id)]
    (hx [:div
         [:p problem]
         [:pre answer]])))

(defn upload! [{{:keys [login id answer]} :params :as request}]
  (tel/log! {:level :info
             :id "upload!"
             :data (dissoc (:params request) :__anti-forgery-token)})
  (try
    (ds/put! {:login login
              :p/id (parse-long id) ; <- ask ds
              :answer answer
              :datetime (jt/local-date-time)})
    (redirect "/workspace")
    (catch Exception e
      (error-page (.getMessage e)))))
