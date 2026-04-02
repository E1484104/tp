# 1cecream03 - Project Portfolio Page

## Project: SudoCook
**SudoCook** is a Java-based Command-Line Interface (CLI) application designed to help users manage recipes and kitchen
inventory efficiently. It enables students and home cooks to track their ingredients and discover what they can cook
through an intuitive text interface.

---

## Summary of Contributions

### 1. Code Contributed
[Link to my code on the tP Code Dashboard](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=1cecream03&breakdown=true&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=)

---

### 2. Enhancements Implemented

* **Set up core application infrastructure** (`SudoCook.java`, `Parser.java`, `Ui.java`)
    * Implemented the main application entry point (`SudoCook`) including the main loop, command
      routing, and storage initialisation.
    * Implemented `Parser` to handle parsing and input validation for all commands, including
      format checks, error messages, and construction of the appropriate `Command` objects.
    * Implemented `Ui` including gradient text rendering, the welcome banner, divider lines,
      error formatting, and user input handling via `Scanner`.

* **Implemented `list-r` and `view-r` commands** (`ListRecipeCommand.java`, `ViewRecipeCommand.java`)
    * `list-r` prints a compact numbered list of all recipe names, giving users a quick overview
      without clutter.
    * `view-r` prints full recipe details (ingredients, steps, time, calories) for all recipes or
      a specific one by 1-based index. Invalid indices are caught and reported with a clear error.

* **Implemented fuzzy search** (`search-r`, `search-i`) (`FuzzySearch.java`, `SearchRecipeCommand.java`, `SearchIngredientCommand.java`)
    * `search-r QUERY` searches the recipe book and `search-i QUERY` searches the inventory, both
      using fuzzy name matching to tolerate typos, partial input, and case differences.
    * Implemented a pure-Java `FuzzySearch` utility class with a priority cascade: exact match →
      substring → character subsequence → Levenshtein distance. No external libraries were used.
    * The match threshold (score ≥ 40) was chosen as the minimum score for a full character
      subsequence match, giving a natural cutoff between recognisable and unrecognisable results.

---

### 3. Contributions to the User Guide (UG)
* Documented `list-r` and `view-r` with format specifications, index rules, usage examples, and
  expected output variants (all recipes, single recipe, invalid index).
* Documented `search-r` and `search-i` with format specifications, examples showing partial and
  typo-tolerant queries, and expected output variants (matches found, no matches).

---

### 4. Contributions to the Developer Guide (DG)
* Authored the `list-r` and `view-r` implementation section:
    * Class responsibility table for the four involved classes.
    * Step-by-step execution walkthrough for both the indexed and non-indexed variants.
    * One Design Consideration aspect with option table and rationale (separating list and view).
* Authored the `search-r` and `search-i` implementation section:
    * Class responsibility table for all six involved classes.
    * Step-by-step execution walkthrough for `search-r` (and noted `search-i` is identical).
    * Fuzzy scoring priority table and key code snippet from `FuzzySearch`.
    * Three Design Consideration aspects with option tables and rationale (scoring strategy,
      match threshold, placement of fuzzy logic).

---

### 5. Contributions to Team-Based Tasks
* Set up the foundational `Parser` and `Ui` classes used by all teammates' commands throughout
  the project, establishing consistent input handling and output formatting conventions.
* Wrote unit tests for `SearchRecipeCommand`, `SearchIngredientCommand`, `UiTest`,
  `ListRecipeCommand`, and `ViewRecipeCommand`.

---

## Contributions to the User Guide (Extracts)

### Listing recipes: `list-r`

Shows the names of all saved recipes in a numbered list.

Format: `list-r`

Expected output:
```
1. Fried Rice
2. Tomato Soup
3. Omelette
```

Expected output (no recipes saved):
```
No recipes found.
```

---

### Viewing recipe details: `view-r`

Shows full details of all recipes, or a specific recipe by index.

Format: `view-r [INDEX]`

* `INDEX` is optional. If omitted, all recipes are shown in full.
* `INDEX` must be a positive integer matching the recipe's position in `list-r`.

Examples:

`view-r`

`view-r 1`

Expected output (specific recipe):
```
Recipe Name: Fried Rice
Preparation Time: 15 minutes
Calories: 400 kcal

    Ingredients:
    - rice (2.0 cups)
    - egg (2.0 pcs)

    Steps:
    - Cook rice
    - Fry eggs
    - Mix
```

Expected output (invalid index):
```
Oops! Index 5 is out of range. (Valid range: 1 to 3)
```

---

### Searching recipes: `search-r`

Fuzzy-searches the recipe book by name. Handles partial input and minor typos.

Format: `search-r QUERY`

Examples:

`search-r fried rice`

`search-r freid` *(typo tolerated)*

Expected output (matches found):
```
Found 1 recipe(s) matching "fried rice":
1. Fried Rice
```

Expected output (no matches):
```
No recipes matched "xyz".
```

---

### Searching ingredients: `search-i`

Fuzzy-searches the inventory by ingredient name. Handles partial input and minor typos.

Format: `search-i QUERY`

Examples:

`search-i tomato`

`search-i tomatto` *(typo tolerated)*

Expected output (matches found):
```
Found 1 ingredient(s) matching "tomato":
1. Tomato (3.0 pcs)
```

Expected output (no matches):
```
No ingredients matched "xyz".
```

---

## Contributions to the Developer Guide (Extracts)

### `list-r` and `view-r` — Recipe Listing and Viewing

#### Overview

Two commands are provided for browsing recipes:

- `list-r` prints a compact numbered list of recipe names only, giving the user a quick overview.
- `view-r` prints full recipe details (ingredients and steps). It can be used with or without an index.

**Command formats:**
- `list-r` — lists all recipe names
- `view-r` — shows full details for all recipes
- `view-r INDEX` — shows full details for the recipe at the given 1-based index

#### Implementation

| Class | Role |
|---|---|
| `Parser` | Detects `list-r` or `view-r` prefix and constructs the appropriate command |
| `ListRecipeCommand` | Calls `RecipeBook.listRecipe()` |
| `ViewRecipeCommand` | Calls `RecipeBook.viewRecipe()` or `RecipeBook.viewRecipe(index)` |
| `RecipeBook` | Builds and prints the output string |

#### Design Considerations

**Aspect: Separating list and view into two commands**

| Option | Pros | Cons |
|---|---|---|
| Separate `list-r` (names) and `view-r` (details) (current) | Quick overview with `list-r`; full details on demand with `view-r` | Two commands to remember |
| Single command always showing full details | Fewer commands | Clutters output when the user only wants a name reminder |

*Decision:* Splitting the commands keeps everyday browsing fast while still allowing full detail inspection when needed.

---

### `search-r` and `search-i` — Fuzzy Search

#### Overview

- `search-r QUERY` searches the recipe book for recipes whose names fuzzy-match the query.
- `search-i QUERY` searches the inventory for ingredients whose names fuzzy-match the query.

**Fuzzy scoring priority cascade:**

| Priority | Condition | Score |
|---|---|---|
| 1 | Exact match (case-insensitive) | 100 |
| 2 | Target contains query as a substring | 90 |
| 3 | Query characters appear as a subsequence in target | 40–80 |
| 4 | Levenshtein distance below threshold | 0–60 |

`FuzzySearch.isMatch()` returns `true` when score ≥ 40.

#### Design Considerations

**Aspect: Scoring strategy**

| Option | Pros | Cons |
|---|---|---|
| Priority cascade (current) | Fast short-circuit for common cases; graceful degradation for typos | Scores are heuristic |
| Pure Levenshtein only | Simple and well-understood | Penalises partial queries like `rice` in `Fried Rice` |

*Decision:* A pure-Java cascade was chosen to avoid external dependencies while handling the most common search patterns.