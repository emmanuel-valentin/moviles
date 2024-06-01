import React from "react";
import { StyleSheet, Text, View } from "react-native";
import { Activity } from "./components/Activity"

export default function App() {
  return (
    <View style={styles.container}>
      <View style={styles.taskWrapper}>
        <Text style={styles.sectionTitle}>Actividades del día</Text>
        <View style={styles.items}>
          <Activity text="Actividad 1" />
          <Activity text="Actividad 2" />
        </View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#9EECFF",
  },
  taskWrapper: {
    paddingTop: 80,
    paddingHorizontal: 20,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: "bold",
  },
  items: {
    marginTop: 30,
  },
});
