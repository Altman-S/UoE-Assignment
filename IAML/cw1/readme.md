# IAML 2021 - INFR11182 (Level 11) Coursework 1
This is the repository for Coursework 1 for IAML 2021.

**N.B.: This is the coursework for the Level 11 version of the course. If you are on the Level 10 course (i.e. likely a UG student), then you should use [this](https://github.com/uoe-iaml/INFR10069-2021-CW1) version instead.**

## Repository Contents

 * `IAML_2021_CW1_INFR11182_Instructions.pdf`: This is the pdf with the question-sheet for the coursework.
 * `Assignment_1.tex`: This is the template you should modify to writeup your coursework.
 * `style.tex`: This is the style file for the coursework template. **Do not modify** this file in any way.
 * `data`: This is a directory (folder) which contains all the data you need to complete the coursework.

## A note on working with GIT
 
 * If you are not familiar with version control systems, we recommend just downloading the repository as a zip file and using it as is. If you would like to use the repository for your own work, you **must** fork the current repository into a private one. Publishing your solution is not permitted, in line with the policy on [Academic Misconduct](https://web.inf.ed.ac.uk/infweb/admin/policies/academic-misconduct).

## Adding your student ID
Please make sure you add your student ID number by uncommenting the line in the `Assignment_1.tex' file, i.e. remove the "\%" sign before the line 
\newcommand{\assignmentAuthorName}{s1234567}.

## Conda Environment

For this coursework you **must** use the same versions of the packages that are specified in the [IAML Labs repository](https://github.com/uoe-iaml/iaml-labs). 
Failing to do will result in you loosing points. 

## Building the PDF

This repository provides a latex template (`Assignment_1.tex`) for generating the final PDF that you will submit. 
You **must not** edit the style or formatting of this document.
The only thing you need to do is enter your student number (see PDF for instructions) and put your answers in the correct answer boxes. 

You have a few options to create the final PDF:
* You can use a free browser based latex editor such as [Overleaf](https://www.overleaf.com). **If you use such a tool, make sure to keep your document private and do not share the link to it.** 
* Also, From a DICE machine you can run `pdflatex Assignment_1.tex` twice from the command line to compile the PDF. 
* As a third option, you can do the same from your own computer, provided you have latex and all the necessary packages installed. Unfortunately, we do not support troubleshooting this step for you. 



## Submitting the Final PDF

Instructions will be provided on the IAML Learn page telling you how to upload your final PDF to Gradescope. 


## FAQs
Updated 1 October 2021
* Piazza private questions to instructions should not be used to request hints
* If a particular hyperparameter or setting for an algorithm is not specified in the question, it is implied that you use the default sklearn settings
* Unless you are requested to implement something yourself you can use the provided sklearn or numpy functions
* For questions where we ask for code (e.g. Question 1d), we do not expect you to re-implement low level functions such as matrix multiplication yourself - use numpy
* For questions where you are asked to plot the the data and the specific split is not specified, you should plot the train data
* Do not split any of the data into train and validation unless requested to do so
