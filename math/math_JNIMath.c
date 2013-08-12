#include <math.h>
#include <stdlib.h>
#include "math_JNIMath.h"


jobjectArray objectArrayFromDoubleMatrix(JNIEnv * env, jdouble ** m, jint r, jint c);
jdouble ** doubleMatrixFromObjectArray(JNIEnv * env, jobjectArray a, jint r, jint c);
void releaseDoubleMatrix(JNIEnv * env, jdouble ** m, jobjectArray a, int r);


JNIEXPORT jint JNICALL Java_math_JNIMath_zero(JNIEnv * env, jclass class) {

    return 0;

}



JNIEXPORT jdouble JNICALL Java_math_JNIMath_lgamma(JNIEnv * env, jclass class, jdouble x) {

    return lgamma(x);

}



JNIEXPORT jobjectArray JNICALL Java_math_JNIMath_multiply(JNIEnv * env, jclass class,
							  jobjectArray m1, jint r1, jint c1,
							  jobjectArray m2, jint r2, jint c2) {

    jdouble ** n1 = doubleMatrixFromObjectArray(env, m1, r1, c1);
    jdouble ** n2 = doubleMatrixFromObjectArray(env, m2, r2, c2);
    jdouble ** p = (jdouble **) malloc(r1 * sizeof(jdouble *));
    int i, j, k;


    /* Normal version: */

/*     for (i = 0; i < r1; i += 1) { */

/* 	p[i] = (jdouble *) malloc(c2 * sizeof(jdouble)); */

/*     } */

/*     for (i = 0; i < r1; i += 1) { */

/* 	for (j = 0; j < c2; j += 1) { */

/* 	    jdouble s = 0; */

/* 	    for (k = 0; k < c1; k += 1) { */

/* 		s += n1[i][k] * n2[k][j]; */

/* 	    } */

/* 	    p[i][j] = s; */

/* 	} */

/*     } */


    /* Transposing version: */

    for (i = 0; i < r1; i += 1) {

	p[i] = (jdouble *) malloc(r2 * sizeof(jdouble));

	for (j = 0; j < r2; j += 1) {

	    jdouble s = 0;

	    for (k = 0; k < c1; k += 1) {

		s += n1[i][k] * n2[j][k];

	    }

	    p[i][j] = s;

	}

    }

    releaseDoubleMatrix(env, n1, m1, r1);
    releaseDoubleMatrix(env, n2, m2, r2);

/*     return objectArrayFromDoubleMatrix(env, p, r1, c2); */

    return objectArrayFromDoubleMatrix(env, p, r1, r2);

}


jobjectArray objectArrayFromDoubleMatrix(JNIEnv * env, jdouble ** m, jint r, jint c) {

    jobjectArray result;
    jdoubleArray * rows;
    jclass doubleArrayClass;
    int i;

    rows = (jdoubleArray *) malloc(r * sizeof(jdoubleArray));

    for (i = 0; i < r; i += 1) {

	rows[i] = (*env)->NewDoubleArray(env, c);
	(*env)->SetDoubleArrayRegion(env, rows[i], 0, c, m[i]);

    }

    doubleArrayClass = (*env)->FindClass(env, "[D");
    result = (*env)->NewObjectArray(env, r, doubleArrayClass, NULL);

    for (i = 0; i < r; i += 1) {

	(*env)->SetObjectArrayElement(env, result, i, rows[i]);

    }

    return result;

}


/* jdouble ** doubleMatrixFromObjectArray(JNIEnv * env, jobjectArray a, jint r, jint c) { */

/*     jdouble ** m; */
/*     jdoubleArray row; */
/*     int i; */

/*     m = (jdouble **) malloc(r * sizeof(jdouble *)); */

/*     for (i = 0; i < r; i += 1) { */

/* 	m[i] = (jdouble *) malloc(c * sizeof(jdouble)); */
/* 	row = (*env)->GetObjectArrayElement(env, a, i); */
/* 	(*env)->GetDoubleArrayRegion(env, row, 0, c, m[i]); */

/*     } */

/*     return m; */

/* } */


jdouble ** doubleMatrixFromObjectArray(JNIEnv * env, jobjectArray a, jint r, jint c) {

    jdouble ** m;
    jdoubleArray row;
    jboolean isCopy;
    int i;

    m = (jdouble **) malloc(r * sizeof(jdouble *));

    for (i = 0; i < r; i += 1) {

	row = (*env)->GetObjectArrayElement(env, a, i);
/* 	m[i] = (*env)->GetDoubleArrayElements(env, row, &isCopy); */
	m[i] = (*env)->GetPrimitiveArrayCritical(env, row, &isCopy);
/* 	printf("\t%d: %s\n", i, ((isCopy == JNI_TRUE) ? "copy" : "not a copy")); */

    }

    return m;

}


void releaseDoubleMatrix(JNIEnv * env, jdouble ** m, jobjectArray a, int r) {

    jdoubleArray row;
    int i;

    for (i = 0; i < r; i += 1) {

	row = (*env)->GetObjectArrayElement(env, a, i);
/* 	(*env)->ReleaseDoubleArrayElements(env, row, m[i], JNI_ABORT); */
	(*env)->ReleasePrimitiveArrayCritical(env, row, m[i], JNI_ABORT);

    }

}
