package com.kdroid.gplayscrapper.constants

// Regex for datasets
internal val keyRegex = Regex("(ds:\\d+)")
internal val valueRegex = Regex("data:([\\s\\S]*?),\\s*sideChannel:")