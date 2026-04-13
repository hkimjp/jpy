(ns user
  (:require
   [clj-reload.core :as reload]
   ; [clojure.java.io :as io]
   ; [charred.api :as charred]
   ; [environ.core :refer [env]]
   [taoensso.telemere :as tel]
   ; [hkimjp.datascript :as ds]
   ; [hkimjp.jpy.util :as util]
   [hkimjp.jpy.system :refer [start-system stop-system restart-system]]))

;--------------------------
(tel/set-min-level! :debug)

(restart-system)

; (start-system)

;---------------------------
; clj-reload
; hooks do not work?
(defn before-ns-unload []
  (println "called before-ns-unload"))

(defn after-ns-reload []
  (println "called after-ns-reload"))

(reload/init
 {:output :verbose
  :dirs ["src" "test"]
  :no-reload '#{user}})

(defn reload []
  (stop-system)
  (reload/reload)
  (start-system))

; (reload)

;-------------------
(comment
  1 情報基礎はどんなふうに授業が進み、成績はどうつくか？
  2 過去の授業、日本語、スペースや @入りのアカウントでどんな事件があったか？
  3 OneDrive ってなんですの？
  4 「OneDrive 配下のデスクトップ」ってどんな意味？その危険性は？
  5 授業資料のページの URL はなんですか？ブックマークした？
  6 hkimura のオフィスは？オフィスを出てみんなの質問を図書館で待ち受ける曜日は？
  :rcf)
