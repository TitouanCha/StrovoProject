

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.R
import com.example.strovo.component.settingScreenComponents.UpdateDisciplineFlowRow
import com.example.strovo.component.settingScreenComponents.UserDisciplineFlowRow
import com.example.strovo.data.model.Discipline

@Composable
fun DisciplineFlowRow(
    userDiscipline: List<Discipline>,
    disciplines: List<Discipline>,
    modifier: Modifier = Modifier,
    isUpdatedDisciplineList: Boolean,
    addDisciplineClick: (List<Discipline>) -> Unit = {}
){
    val updatedDisciplineList = remember { mutableStateOf<List<Discipline>>(userDiscipline) }
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Disciplines :",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(3f)
            )
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                onClick = {
                    addDisciplineClick(updatedDisciplineList.value)
                }
            ){
                Icon(
                    painter = painterResource(id =
                        if(isUpdatedDisciplineList)
                            R.drawable.tick_svgrepo_com
                        else
                            R.drawable.baseline_add_24),
                    contentDescription = "Settings Icon",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if(!isUpdatedDisciplineList){
            UserDisciplineFlowRow(userDiscipline)
        }else {
            UpdateDisciplineFlowRow(
                updatedDisciplineList.value,
                disciplines,
                onAddClick = { newDiscipline ->
                    updatedDisciplineList.value = updatedDisciplineList.value + newDiscipline
                },
                onDeleteClick = { deletedDiscipline ->
                    updatedDisciplineList.value = updatedDisciplineList.value - deletedDiscipline
                }
            )
        }
    }
}