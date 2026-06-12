#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct {
char *data;
size_t length;

size_t capacity;
} StringBuffer;

/* Function to initialize StringBuffer */
StringBuffer* sb_init(size_t initial_capacity) {
StringBuffer *sb = (StringBuffer *)malloc(sizeof(StringBuffer));

if (sb == NULL) {
printf("Memory allocation failed for StringBuffer.\n");
return NULL;
}

sb->data = (char *)malloc(initial_capacity * sizeof(char));

if (sb->data == NULL) {
printf("Memory allocation failed for data buffer.\n");
free(sb);
return NULL;
}

sb->length = 0;
sb->capacity = initial_capacity;

/* Empty string */
sb->data[0] = '\0';

return sb;
}

/* Function to append string */
void sb_append(StringBuffer *sb, const char *str) {
if (sb == NULL || str == NULL)
return;

size_t str_len = strlen(str);

/* Check if capacity is enough */
while (sb->length + str_len + 1 > sb->capacity) {

size_t new_capacity = sb->capacity * 2;

/* Safe realloc */
char *temp = (char *)realloc(sb->data, new_capacity);

if (temp == NULL) {
printf("Realloc failed. Buffer unchanged.\n");
return;
}

sb->data = temp;
sb->capacity = new_capacity;

printf("Buffer grown! New capacity = %zu\n", sb->capacity);
}

/* Append string */

strcpy(sb->data + sb->length, str);

sb->length += str_len;
}

/* Destructor function */
void sb_free(StringBuffer *sb) {
if (sb != NULL) {
free(sb->data);
free(sb);
}
}

/* Main function for demonstration */
int main() {

/* Small initial capacity */
StringBuffer *sb = sb_init(8);

if (sb == NULL)
return 1;

printf("Initial Capacity = %zu\n", sb->capacity);

sb_append(sb, "Hello");
printf("String: %s\n", sb->data);
printf("Length: %zu, Capacity: %zu\n\n",
sb->length, sb->capacity);

sb_append(sb, " World");
printf("String: %s\n", sb->data);
printf("Length: %zu, Capacity: %zu\n\n",
sb->length, sb->capacity);

sb_append(sb, " This is a dynamic string buffer in C.");
printf("String: %s\n", sb->data);
printf("Length: %zu, Capacity: %zu\n\n",
sb->length, sb->capacity);

/* Free memory */
sb_free(sb);

printf("All memory freed successfully.\n");
return 0;
}