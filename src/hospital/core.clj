(ns hospital.core
  (:use [clojure.pprint])
  (:require [hospital.model :as h.model]))


; espera FILA DE ESPERA 3
; laboratorio1 3
; laboratorio2 2
; laboratorio3

(let [hospital-do-david (h.model/novo-hospital)]
  (pprint hospital-do-david))

(pprint h.model/fila-vazia)