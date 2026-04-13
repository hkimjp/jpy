(ns hkimjp.jpy.event
  (:require
   [org.httpkit.server :as hk]
   [hiccup2.core :as h]
   [taoensso.telemere :as tel]))

(defonce clients (atom #{}))

(defn reset-clients! [_]
  (tel/log! :info "reset-clients!")
  (reset! clients #{})
  {:status 302
   :headers {"Location" "/admin"}
   :body ""})

(defn format-event [body]
  (str "data: " body "\n\n"))

(defn send! [ch message]
  (hk/send! ch
            {:status  200
             :headers {"Content-Type"      "text/event-stream"
                       "Cache-Control"     "no-cache,no-store"}
             :body    (format-event message)}
            false))

(defn event [req]
  (tel/log! {:level :info :id "event"})
  (hk/as-channel req
                 {:on-open  (fn [ch]   (swap! clients conj ch))
                  :on-close (fn [ch _] (swap! clients disj ch))}))

(defn broadcast-message-to-connected-clients! [message]
  (tel/log! {:level :info :id "boradcast-..." :msg message
             :data (count @clients)})
  (run! (fn [ch] (send! ch message)) @clients))

(defn broadcast! [{{:keys [message]} :params}]
  (tel/log! {:level :info :id "broadcast!" :msg message})
  (broadcast-message-to-connected-clients! message))

(defn number-of-clients []
  (count @clients))

(comment
  ;; Open a terminal and connect
  ;; http :8080/event

  (broadcast-message-to-connected-clients! (str (java.util.Date.)))

  (broadcast-message-to-connected-clients!
   (str "<div>Nice to meet <b>you</b><p>paragraph</p></div>"))

  (broadcast-message-to-connected-clients!
   (str (h/html [:div.text-2xl.font-bold.text-red-600 [:p "Hello"]])))

  @clients
  (count @clients)
  :rcf)
