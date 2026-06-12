#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define NUM_THREADS            8
#define INCREMENTS_PER_THREAD  100000
#define EXPECTED               ((long long)NUM_THREADS * INCREMENTS_PER_THREAD)

/* ── Shared state ───────────────────────────────────────────────────── */
long long       counter = 0;
pthread_mutex_t counter_mutex = PTHREAD_MUTEX_INITIALIZER;

/* ── Thread: NO synchronization ─────────────────────────────────────── */
void *increment_no_sync(void *arg) {
    for (int i = 0; i < INCREMENTS_PER_THREAD; i++)
        counter++;          /* race condition */
    return NULL;
}

/* ── Thread: WITH mutex ──────────────────────────────────────────────── */
void *increment_with_mutex(void *arg) {
    for (int i = 0; i < INCREMENTS_PER_THREAD; i++) {
        pthread_mutex_lock(&counter_mutex);
        counter++;          /* protected critical section */
        pthread_mutex_unlock(&counter_mutex);
    }
    return NULL;
}

/* ── Helper: run a test with any thread function ─────────────────────── */
void run_test(const char *label, void *(*thread_fn)(void *)) {
    pthread_t threads[NUM_THREADS];

    counter = 0;            /* reset before each run */

    for (int t = 0; t < NUM_THREADS; t++)
        pthread_create(&threads[t], NULL, thread_fn, NULL);

    for (int t = 0; t < NUM_THREADS; t++)
        pthread_join(threads[t], NULL);

    printf("%-30s Expected: %-10lld  Actual: %-10lld  %s\n",
           label, EXPECTED, counter,
           counter == EXPECTED ? "✓ CORRECT" : "✗ WRONG (lost updates)");
}

/* ── Main ────────────────────────────────────────────────────────────── */
int main(void) {
    printf("Threads: %d  |  Increments per thread: %d  |  Expected total: %lld\n\n",
           NUM_THREADS, INCREMENTS_PER_THREAD, EXPECTED);

    run_test("Without mutex (race):", increment_no_sync);
    run_test("With mutex (safe):   ", increment_with_mutex);

    printf("\nRun the program multiple times — the 'no mutex' result changes each time!\n");

    pthread_mutex_destroy(&counter_mutex);
    return 0;
}