;practic lisp 


;exista (l: list, e: int)
;l:lista initiala
;e: element cautat in lista

(defun exista (l e)
	(cond
		((null l) nil)
		((equal e (car l)) t)
		(t (exista (cdr l) e))
	)
)


;multime (l: list, rez: list)
;l:lista initiala
;rez: lista rezultata fara dubluri

(defun multime (l rez)
	(cond
		((null l) rez)
		((not (exista (cdr l) (car l))) (multime (cdr l) (cons (car l) rez)))
		(t (multime (cdr l) rez))
	)
)


(defun mainMultime (l)
	(multime l nil)
)


;minim (l: list, m: int)
;l:lista initiala
;m: cel mai mic element al listei

(defun minim (l m)
	(cond
		((null l) m)
		((or (= (car l) m)(< (car l) m) ) (minim (cdr l) (car l)))
		(t (minim (cdr l) m))
	)
)

(defun mainMinim (l)
	(minim l (car l))
)


;sortare (l: list, rez: list)
;l:lista initiala
;rez: lista sortata

(defun sortare (l rez) 
	(cond
		((null l) rez)
		((equal (car l)(mainMinim l) ) (sortare (cdr l) (append rez (list (car l)))))
		(t (sortare (append (cdr l) (list (car l))) rez))
	)
)

(defun mainSortare (l)
	(sortare (mainMultime l) nil)
)

