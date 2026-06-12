import { useState } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, SafeAreaView } from 'react-native';

export default function App() {
  const [count, setCount] = useState(0);
  const [isDarkMode, setIsDarkMode] = useState(false);

  // Counter handlers
  const handleIncrement = () => setCount(prev => prev + 1);
  const handleDecrement = () => setCount(prev => (prev > 0 ? prev - 1 : 0));
  const handleReset = () => setCount(0);

  // Theme handler
  const toggleTheme = () => setIsDarkMode(prev => !prev);

  // Theme colors
  const theme = {
    bg: isDarkMode ? '#111827' : '#F9FAFB',
    card: isDarkMode ? '#1F2937' : '#FFFFFF',
    accent: isDarkMode ? '#6EE7B7' : '#059669',
    accentText: '#FFFFFF',
    text: isDarkMode ? '#F9FAFB' : '#111827',
    subtext: isDarkMode ? '#9CA3AF' : '#6B7280',
    border: isDarkMode ? '#374151' : '#E5E7EB',
    decrementBg: isDarkMode ? '#374151' : '#F3F4F6',
    decrementText: isDarkMode ? '#F9FAFB' : '#374151',
    resetBg: isDarkMode ? '#292524' : '#FEF3C7',
    resetText: isDarkMode ? '#FCA5A5' : '#B45309',
    toggleBg: isDarkMode ? '#4B5563' : '#E0F2FE',
    toggleText: isDarkMode ? '#E0F9FF' : '#0369A1',
  };

  const digitColor =
    count === 0 ? theme.subtext : count >= 50 ? '#DC2626' : theme.accent;

  return (
    <SafeAreaView style={[styles.safeArea, { backgroundColor: theme.bg }]}>
      <View style={[styles.container, { backgroundColor: theme.bg }]}>

        {/* Header */}
        <View style={styles.header}>
          <Text style={[styles.appTitle, { color: theme.accent }]}>COUNTER</Text>
          <Text style={[styles.appSubtitle, { color: theme.subtext }]}>tap to count</Text>
        </View>

        {/* Card */}
        <View style={[styles.card, { backgroundColor: theme.card, borderColor: theme.border }]}>
          <View style={[styles.cardAccentBar, { backgroundColor: theme.accent }]} />

          <Text style={[styles.label, { color: theme.subtext }]}>CURRENT COUNT</Text>

          {/* Digit Display */}
          <View style={[styles.digitContainer, { backgroundColor: theme.bg, borderColor: theme.border }]}>
            <Text style={[styles.digit, { color: digitColor }]}>
              {String(count).padStart(3, '0')}
            </Text>
          </View>

          <Text style={[styles.statusText, { color: theme.subtext }]}>
            {count === 0 ? 'Start counting!' : count >= 50 ? 'High count! 🔥' : `${count} increment${count !== 1 ? 's' : ''} recorded`}
          </Text>

          {/* Increment / Decrement Row */}
          <View style={styles.primaryRow}>
            <TouchableOpacity
              style={[styles.btnBase, { backgroundColor: theme.decrementBg, borderColor: theme.border, borderWidth: 1, flex: 1, marginRight: 6 }]}
              onPress={handleDecrement}
              activeOpacity={0.7}
            >
              <Text style={[styles.btnSymbol, { color: theme.decrementText }]}>−</Text>
              <Text style={[styles.btnLabel, { color: theme.decrementText }]}>DECREMENT</Text>
            </TouchableOpacity>

            <TouchableOpacity
              style={[styles.btnBase, { backgroundColor: theme.accent, flex: 1, marginLeft: 6 }]}
              onPress={handleIncrement}
              activeOpacity={0.7}
            >
              <Text style={[styles.btnSymbol, { color: theme.accentText }]}>+</Text>
              <Text style={[styles.btnLabel, { color: theme.accentText }]}>INCREMENT</Text>
            </TouchableOpacity>
          </View>

          {/* Reset Button */}
          <TouchableOpacity
            style={[styles.btnBase, styles.btnReset, { backgroundColor: theme.resetBg, borderColor: theme.border }]}
            onPress={handleReset}
            activeOpacity={0.7}
          >
            <Text style={[styles.btnSymbol, { color: theme.resetText, fontSize: 18 }]}>↺</Text>
            <Text style={[styles.btnLabel, { color: theme.resetText }]}>  RESET TO ZERO</Text>
          </TouchableOpacity>
        </View>

        {/* Theme Toggle */}
        <TouchableOpacity
          style={[styles.toggleBtn, { backgroundColor: theme.toggleBg, borderColor: theme.border }]}
          onPress={toggleTheme}
          activeOpacity={0.7}
        >
          <Text style={styles.toggleIcon}>{isDarkMode ? '☀️' : '🌙'}</Text>
          <Text style={[styles.toggleText, { color: theme.toggleText }]}>
            {isDarkMode ? 'Switch to Light Mode' : 'Switch to Dark Mode'}
          </Text>
        </TouchableOpacity>

        <Text style={[styles.footer, { color: theme.subtext }]}>React Native Counter App</Text>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
  },
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    paddingHorizontal: 24,
  },
  header: {
    alignItems: 'center',
    marginBottom: 32,
  },
  appTitle: {
    fontSize: 13,
    fontWeight: '700',
    letterSpacing: 4,
    marginBottom: 4,
  },
  appSubtitle: {
    fontSize: 11,
    letterSpacing: 2,
  },
  card: {
    borderRadius: 20,
    padding: 28,
    width: '100%',
    alignItems: 'center',
    borderWidth: 1,
    elevation: 8,
    overflow: 'hidden',
  },
  cardAccentBar: {
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    height: 4,
  },
  label: {
    fontSize: 10,
    letterSpacing: 3,
    marginTop: 12,
    marginBottom: 16,
  },
  digitContainer: {
    borderRadius: 14,
    paddingVertical: 20,
    paddingHorizontal: 36,
    marginBottom: 8,
    borderWidth: 1,
    minWidth: 200,
    alignItems: 'center',
    justifyContent: 'center',
  },
  digit: {
    fontSize: 72,
    fontWeight: '700',
    letterSpacing: 2,
  },
  statusText: {
    fontSize: 12,
    marginBottom: 24,
    letterSpacing: 1,
  },
  primaryRow: {
    flexDirection: 'row',
    width: '100%',
    marginBottom: 12,
  },
  btnBase: {
    borderRadius: 12,
    paddingVertical: 14,
    paddingHorizontal: 16,
    alignItems: 'center',
    justifyContent: 'center',
  },
  btnReset: {
    width: '100%',
    borderWidth: 1,
    flexDirection: 'row',
    justifyContent: 'center',
  },
  btnSymbol: {
    fontSize: 24,
    fontWeight: '700',
  },
  btnLabel: {
    fontSize: 9,
    letterSpacing: 2,
    fontWeight: '700',
    marginTop: 4,
  },
  toggleBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 24,
    paddingVertical: 12,
    paddingHorizontal: 24,
    borderRadius: 50,
    borderWidth: 1,
  },
  toggleIcon: {
    fontSize: 18,
    marginRight: 8,
  },
  toggleText: {
    fontSize: 12,
    fontWeight: '700',
    letterSpacing: 1,
  },
  footer: {
    marginTop: 20,
    fontSize: 10,
    letterSpacing: 2,
    opacity: 0.6,
  },
});