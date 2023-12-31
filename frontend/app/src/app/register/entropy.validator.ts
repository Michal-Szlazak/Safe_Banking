export function entropyValidator(control: { value: string; }) {

  const password = control.value;
  const entropy = calculateEntropy(password);
  return entropy >= 60 ? null : { 'weakPassword': true };
}
function calculateEntropy(password: string): number {
  const poolSize = calculatePoolSize(password);
  return Math.log2(Math.pow(poolSize, password.length));
}

function calculatePoolSize(password: string): number {
  let pool = 0;

  if(/[a-z]/.test(password)) {
    pool += 26;
  }
  if(/[A-Z]/.test(password)) {
    pool += 26;
  }
  if(/[0-9]/.test(password)) {
    pool += 10;
  }
  if(/[!@#$%^&*()-_=+[]{}|;:,.<>?]/.test(password)) {
    pool += 26;
  }

  return pool;
}
