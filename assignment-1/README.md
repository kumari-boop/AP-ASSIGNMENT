# AP ASSIGNMENT

## Question

Write a C program to analyze time complexities in constant time, linear time and quadratic

## solution

#include <stdio.h>

void linear(int n) {
for (int i = 0; i < n; i++);
}

void logarithmic(int n) {
for (int i = n; i > 1; i /= 2);
}

void quadratic(int n) {
for (int i = 0; i < n; i++)
for (int j = 0; j < n; j++);
}

int main() {
int n[] = {1000, 5000, 10000};
clock_t start, end;

for (int i = 0; i < 3; i++) {

printf("\nInput size: %d\n", n[i]);

start = clock();
linear(n[i]);
end = clock();
printf("Linear Time: %lf\n", (double)(end - start));

start = clock();
logarithmic(n[i]);
end = clock();
printf("Logarithmic Time: %lf\n", (double)(end - start));

start = clock();
quadratic(n[i]);
end = clock();
printf("Quadratic Time: %lf\n", (double)(end - start));
}
return 0;
}