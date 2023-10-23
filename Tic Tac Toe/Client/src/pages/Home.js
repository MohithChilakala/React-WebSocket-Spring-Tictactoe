import { CreateRoom } from '../component/home/CreateRoom';
import { UserProfile } from '../component/home/UserProfile'
import '../styles/home.css';

const Home = () => {
  return (
    <div>
      <UserProfile />
      <CreateRoom />
    </div>
  );
};

export default Home;
