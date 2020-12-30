package com.example.koshelek_trial_task.data_classes

import com.example.koshelek_trial_task.data_classes.SingleEntity

data class Entities (
        var type: String,
        var values: MutableList<SingleEntity>
)